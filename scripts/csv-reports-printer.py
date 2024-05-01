import argparse
import csv
import sys
import typing
from text_colorize import ANSIColors, TextStyle, colorize


COLUMNS_TYPES = [
    '_MISSED',
    '_COVERED',
]

CSV_COLUMNS = [
    'PACKAGES',
    'CLASS',
    'BRANCH_MISSED',
    'BRANCH_COVERED',
    'LINE_MISSED',
    'LINE_COVERED',
    'METHOD_MISSED',
    'METHOD_COVERED',
]
DISPLAY_COLUMNS = [
    'PACKAGES',
    'CLASS',
    'BRANCH',
    'LINE',
    'METHOD',
]
DEFAULT_LABEL_SIZE = 8


def create_row_info() -> dict:
    return {
        key: 0 for key in CSV_COLUMNS
    }


def is_valid_lib(group: str, lib_name: str) -> bool:
    if len(lib_name) == 0:
        return True
    return group == lib_name


def parse_args(args: list[str]) -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        prog="CSV Jacoco Test Reports Printer",
        description="Program read csv file with jacoco report and print it at terminal at stdin",
    )

    parser.add_argument(
        "-i", "--input", required=True,
        help="setup path to CSV file with jacoco report information",
        metavar="<?>"
    )
    parser.add_argument(
        "-l", "--lib",
        help="setup library name to remove from package path",
        default="",
        metavar="<?>"
    )
    parser.add_argument(
        "-p", "--package-print",
        help="setup flag to 'ON' to print packages of files at report (default 'OFF')",
        action='store_true',
        default=False
    )

    return parser.parse_args(args)


def read_csv(namespace: argparse.Namespace) -> typing.Optional[dict]:
    table = []
    max_packages_name_length = 20
    max_classes_name_length = 20

    with open(getattr(namespace, "input"), 'r') as file:
        reader = csv.reader(file)
        for row in reader:
            if len(row) == 0: 
                break
            if (row[0] == "GROUP") or not is_valid_lib(row[0], getattr(namespace, "lib", "")):
                continue

            row_info = create_row_info()
            is_skipped = False
            for key in row_info.keys():
                if key not in CSV_COLUMNS:
                    row_info.pop(key)

                index = CSV_COLUMNS.index(key) + 1
                row_info[key] = row[index]

                if key == "PACKAGES":
                    max_packages_name_length = max(max_packages_name_length, len(row_info[key]))
                elif key == "CLASS":
                    if '(' in row_info[key] or ')' in row_info[key] or ' ' in row_info[key]:
                        is_skipped = True
                        break
                    elif '.' in row_info[key]:
                         row_info[key] =  row_info[key].split('.')[-1]

                    max_classes_name_length = max(max_classes_name_length, len(row_info[key]))

            if not is_skipped:
                table.append(row_info)
    return {
        "table": table,
        "max_packages_name_length": max_packages_name_length,
        "max_classes_name_length": max_classes_name_length
    }


def create_label(text: str, lbl_size: int, color: ANSIColors = ANSIColors.BLACK):
    if len(text) >= lbl_size:
        text = text[:lbl_size]

    if len(text) % 2 != 0:
        text = ' ' + text

    color_text = colorize(
        f"{{:^{lbl_size}}}".format(text),
        color,
        TextStyle.BOLD
    )
    return f'| {color_text} |'


def colorize_percent_label(percent: int) -> str:
    color = ANSIColors.RED
    if 50 <= percent < 75:
        color = ANSIColors.YELLOW
    elif 75 <= percent <= 100:
        color = ANSIColors.GREEN

    return create_label(f"{percent}%", DEFAULT_LABEL_SIZE, color)


def display_columns(max_packages_name_length: int, max_classes_name_length: int) -> int:
    global DISPLAY_COLUMNS, DEFAULT_LABEL_SIZE
    for column in DISPLAY_COLUMNS:
        lbl_size = DEFAULT_LABEL_SIZE
        if column == "CLASS":
            lbl_size = max_classes_name_length
        elif column == "PACKAGES":
            lbl_size = max_packages_name_length

        lbl = create_label(column, lbl_size, ANSIColors.PURPLE)
        print(lbl, end="")
    print()


def display_csv_data(namespace: argparse.Namespace, csv_data_dict: dict) -> None:
    global DISPLAY_COLUMNS, COLUMNS_TYPES
    if not getattr(namespace, "package_print", False):
        DISPLAY_COLUMNS.remove("PACKAGES")

    if getattr(namespace, 'lib'):
        print(f"Jacoco Covered Report Info for module named '{getattr(namespace, 'lib')}':")

    max_packages_name_length = csv_data_dict.get("max_packages_name_length", 20)
    max_classes_name_length = csv_data_dict.get("max_classes_name_length", 20)
    table: list[dict] = csv_data_dict.get("table", [])
    display_columns(max_packages_name_length, max_classes_name_length)

    for row in table:
        for column in DISPLAY_COLUMNS:
            lbl = ""
            if column in ["PACKAGES", "CLASS"]:
                lbl = create_label(
                    row[column],
                    max_packages_name_length if column == "PACKAGES" else max_classes_name_length,
                    ANSIColors.YELLOW
                )
            else:
                vals = [int(row[column + _type]) for _type in COLUMNS_TYPES]
                percent = int(round((vals[1] - vals[0]) / vals[1], 2) * 100) if vals[1] != 0 else 100
                lbl = colorize_percent_label(
                    percent,
                )

            print(lbl, end="")
        print()


if __name__ == "__main__":
    ns = parse_args(sys.argv[1:])

    try:
        csv_data = read_csv(ns)
    except Exception as e:
        print(
            f"Can't read csv file: '{getattr(ns, 'input')}', get exception: '{e}'",
            file=sys.stderr
        )
        sys.exit(-1)

    display_csv_data(ns, csv_data)

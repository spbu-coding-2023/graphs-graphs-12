import argparse
import sys, os
import xml.etree.ElementTree as ET
from text_colorize import ANSIColors, TextStyle, colorize


class TestCase:

    def __init__(self, name: str, is_passed: bool) -> None:
        self.name = str(name)
        self.is_passed = bool(is_passed)

    def toString(self, indent: int = 0):
        return "\t" * indent + f"{self.name} -> {self.result_type()}"

    def result_type(self) -> str:
        return colorize(
            'PASSED' if self.is_passed else 'FAILURE',
            ANSIColors.GREEN if self.is_passed else ANSIColors.RED,
            TextStyle.ITALIC
        )
    
    def __bool__(self):
        return self.is_passed


class ParametrisedTestCase(TestCase):

    def __init__(self, name: str, cases: list[TestCase]) -> None:
        super().__init__(name, all(cases))
        self.cases = cases

    def add_case(self, case: TestCase):
        self.is_passed = self.is_passed and case.is_passed
        self.cases.append(case)

    def toString(self, indent: int = 0, also_failed: bool = False):
        inline_cases = []
        for case in self.cases:
            if (also_failed and case.is_passed):
                continue

            inline_cases.append(case.toString(indent + 1))

        inline_cases = '\n'.join(inline_cases).rstrip()
        return super().toString(indent) + f"\n{inline_cases}"


def parse_args(args: list[str]) -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        prog="Test Result Printer",
        description="Program read xml files with tests results and print it at terminal at stdin",
    )
    parser.add_argument(
        "-d", "--dir", required=True,
        help="setup path to directory with  xml files with tests information",
        metavar="<?>"
    )
    parser.add_argument(
        "-a", "--all",
        action="store_true",
        help="setup mode of diplay type to print all test information to ON (default OFF)",
    )
    parser.add_argument(
        "-f", "--all-failures",
        action="store_true",
        help="setup mode of diplay type to print also test information which failed to ON (default OFF)",
    )
    return parser.parse_args(args)


def parse_test_result(test_path: str) -> tuple[ET.Element, dict[str, TestCase]]:
    tree_root = ET.parse(test_path).getroot()
    cases = dict()
    for child in tree_root:
        if child.tag != "testcase":
            continue

        name = child.get("name", "uncknown test cases")
        is_passed = child.find("failure") is None
        if '[' in name:
            primary_name = name.split('[')[0]
            args = '[' + "[".join(name.split('[')[1:])

            case: ParametrisedTestCase = cases.get(primary_name, ParametrisedTestCase(primary_name, []))
            case.add_case(TestCase(args, is_passed))
            cases[primary_name] = case
        else:
            cases[name] = TestCase(name, is_passed)
            
    return (tree_root, cases,)


def display_all_test_result(tree_root: ET.Element, cases: dict[str, TestCase]):
    print(
        "Tests of",
        tree_root.attrib.get("name", "UncnownTestSuite").split('.')[-1].replace("Test", ":"),
        sep=" "
    )
    for name in sorted(cases.keys()):
        print(cases[name].toString(indent=1))

    passed_test_count = int(tree_root.attrib.get("tests", 0)) - int(tree_root.attrib.get("failures", 0))
    print(
        colorize(f"Passed: {passed_test_count}", ANSIColors.GREEN, TextStyle.BOLD),
        colorize(f"Failures: {tree_root.attrib.get('failures', 0)}", ANSIColors.RED, TextStyle.BOLD),
        f"Time: {tree_root.attrib.get('time', 0.0)}",
        sep=" ",
        end=os.linesep * 2
    )

 
def display_failures_test_result(tree_root: ET.Element, cases: dict[str, TestCase]):
    failed_tests = []
    for name in sorted(cases.keys()):
        if not cases[name].is_passed:
            failed_tests.append(cases[name])

    if len(failed_tests) == 0:
        return

    print(
        "Failed tests of",
        tree_root.attrib.get("name", "UncnownTestSuite").split('.')[-1].replace("Test", ":"),
        sep=" "
    )
    for case in failed_tests:
        if isinstance(case, ParametrisedTestCase):
            print(case.toString(indent=1, also_failed=True))
        elif isinstance(case, TestCase):
            print(case.toString(indent=1))

    passed_test_count = int(tree_root.attrib.get("tests", 0)) - int(tree_root.attrib.get("failures", 0))
    print(
        colorize(f"Passed: {passed_test_count}", ANSIColors.GREEN, TextStyle.BOLD),
        colorize(f"Failures: {tree_root.attrib.get('failures', 0)}", ANSIColors.RED, TextStyle.BOLD),
        f"Time: {tree_root.attrib.get('time', 0.0)}",
        sep=" ",
        end=os.linesep * 2
    )


if __name__ == "__main__":
    ns = parse_args(sys.argv[1:])

    tests_result_dir = getattr(ns, "dir")
    childs = os.listdir(tests_result_dir)
    tests_results: list[tuple[ET.Element, dict[str, TestCase]]] = []
    for child in sorted(childs):
        child_path = os.path.join(tests_result_dir, child)
        if not os.path.isfile(child_path):
            continue

        if not (child.startswith("TEST") and child.endswith(".xml")):
            continue

        try:
            tests_results.append(parse_test_result(child_path))
        except Exception as e:
            print(f"Can't display ttest information at file '{child}': {e}", file=sys.stderr)
    
    tests_count = 0
    tests_failed_count = 0
    time_of_all_tests = 0
    for test_result in tests_results:
        tree_root: ET.Element = test_result[0]
        tests_count += int(tree_root.attrib.get("tests", 0))
        tests_failed_count += int(tree_root.attrib.get("failures", 0))
        time_of_all_tests += float(tree_root.attrib.get('time', 0.0))
    
    
    print(
        colorize(f"Count of tests: {tests_count}", ANSIColors.YELLOW, TextStyle.BOLD),
        colorize(f"Count of passed tests: {tests_count - tests_failed_count}", ANSIColors.GREEN, TextStyle.BOLD),
        colorize(f"Count of failured tests: {tests_failed_count}", ANSIColors.RED, TextStyle.BOLD),
        colorize(f"Time: {time_of_all_tests}", ANSIColors.BLUE, TextStyle.BOLD),
        sep=os.linesep,
        end=os.linesep * 2
    )
    
    for test_result in tests_results:
        if getattr(ns, "all", False):        
            display_all_test_result(test_result[0], test_result[1])
        elif getattr(ns, "all_failures", False):
            display_failures_test_result(test_result[0], test_result[1])

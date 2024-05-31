import enum

class ANSIColors(enum.IntEnum):
    PURPLE = 35
    BLUE = 34
    GREEN = 32
    YELLOW = 33
    RED = 31
    BLACK = 30
    WHITE = 37


class TextStyle(enum.IntEnum):
    SIMPLE = 0
    BOLD = 1
    ITALIC = 3


def colorize(text: str, color: ANSIColors, style: TextStyle = TextStyle.SIMPLE) -> str:    
    return f"\033[{style};{color}m{text}\033[0m"

enum Color {
    RED = "RED",
    ORANGE = "ORANGE",
    WHITE = "WHITE",
    BROWN = "BROWN"
}

export const colorsToString = new Map<Color, string>()
colorsToString.set(Color.RED, "Красный")
colorsToString.set(Color.ORANGE, "Оранжевый")
colorsToString.set(Color.WHITE, "Белый")
colorsToString.set(Color.BROWN, "Коричневый")

export function stringToColor(color: string) {
    switch (color.toUpperCase()) {
        case "RED": return Color.RED
        case "ORANGE": return Color.ORANGE
        case "WHITE": return Color.WHITE
        case "BROWN": return Color.BROWN
        default: return null
    }
}

export default Color
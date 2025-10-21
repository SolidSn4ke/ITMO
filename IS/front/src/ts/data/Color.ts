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

export default Color
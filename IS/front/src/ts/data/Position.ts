enum Position {
    DIRECTOR = 'DIRECTOR',
    COOK = "COOK",
    CLEANER = 'CLEANER',
    MANAGER_OF_CLEANING = 'MANAGER_OF_CLEANING'
}

export const positionToString = new Map<Position, string>()
positionToString.set(Position.DIRECTOR, "Управляющий")
positionToString.set(Position.COOK, "Повар")
positionToString.set(Position.CLEANER, "Уборщик")
positionToString.set(Position.MANAGER_OF_CLEANING, "Менеджер по клинингу")

export default Position
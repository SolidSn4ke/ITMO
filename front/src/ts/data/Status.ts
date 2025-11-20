enum Status {
    FIRED = 'FIRED',
    RECOMMENDED_FOR_PROMOTION = "RECOMMENDED_FOR_PROMOTION",
    REGULAR = 'REGULAR',
    PROBATION = 'PROBATION'
}

export const statusToString = new Map<Status, string>()
statusToString.set(Status.FIRED, "Уволен")
statusToString.set(Status.RECOMMENDED_FOR_PROMOTION, "Рекомендован к повышению")
statusToString.set(Status.REGULAR, "Сотрудник")
statusToString.set(Status.PROBATION, "Испытательный срок")

export function stringToStatus(status: string) {
    switch (status.toUpperCase()) {
        case "FIRED":
            return Status.FIRED
        case "RECOMMENDED_FOR_PROMOTION":
            return Status.RECOMMENDED_FOR_PROMOTION
        case "REGULAR":
            return Status.REGULAR
        case "PROBATION":
            return Status.PROBATION
        default:
            return null
    }
}

export default Status
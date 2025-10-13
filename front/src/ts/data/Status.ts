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

export default Status
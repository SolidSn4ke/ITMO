enum OrganizationType {
    COMMERCIAL = 'COMMERCIAL',
    GOVERNMENT = 'GOVERNMENT',
    TRUST = 'TRUST',
    PRIVATE_LIMITED_COMPANY = 'PRIVATE_LIMITED_COMPANY',
    OPEN_JOINT_STOCK_COMPANY = 'OPEN_JOINT_STOCK_COMPANY'
}

export const organizationTypeToString = new Map<OrganizationType, string>()
organizationTypeToString.set(OrganizationType.COMMERCIAL, "Коммерческая")
organizationTypeToString.set(OrganizationType.GOVERNMENT, "Правительственная")
organizationTypeToString.set(OrganizationType.TRUST, "Траст")
organizationTypeToString.set(OrganizationType.PRIVATE_LIMITED_COMPANY, "Частная компания")
organizationTypeToString.set(OrganizationType.OPEN_JOINT_STOCK_COMPANY, "Открытое акционерное общество")

export const stringToOrganizationType = (orgType: string) => {
    switch (orgType) {
        case 'GOVERNMENT':
            return OrganizationType.GOVERNMENT
        case 'TRUST':
            return OrganizationType.TRUST
        case 'PRIVATE_LIMITED_COMPANY':
            return OrganizationType.PRIVATE_LIMITED_COMPANY
        case 'OPEN_JOINT_STOCK_COMPANY':
            return OrganizationType.OPEN_JOINT_STOCK_COMPANY
        default:
            return OrganizationType.COMMERCIAL
    }
}


export default OrganizationType
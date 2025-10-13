import {useMemo, useState} from 'react'
import Worker from '../data/Worker'

type SortField = keyof Worker | 'coordinates' | 'organization' | 'person'
type SortDirection = 'asc' | 'desc'
type FilterOperator = '=' | '>' | '<' | '~'

interface FilterCondition {
    field: string
    operator: FilterOperator
    value: string
}

interface SortConfig {
    field: SortField
    direction: SortDirection
}

interface PaginationConfig {
    currentPage: number
}

export function useWorkerTable(items: Worker[]) {
    const [sortConfig, setSortConfig] = useState<SortConfig>({
        field: 'id',
        direction: 'asc'
    })

    const [filterInput, setFilterInput] = useState('')
    const [pagination, setPagination] = useState<PaginationConfig>({
        currentPage: 1
    })

    const filterConditions = useMemo((): FilterCondition[] => {
        if (!filterInput || filterInput.trim() === '') {
            return []
        }

        const conditions: FilterCondition[] = []
        const parts = filterInput.split(/\s+/)

        for (const part of parts) {
            if (!part) continue

            const match = part.match(/(\w+)([=><~])(.+)/)
            if (match) {
                const [, field, operator, value] = match
                if (field && operator && value) {
                    conditions.push({
                        field,
                        operator: operator as FilterOperator,
                        value
                    })
                }
            }
        }

        return conditions
    }, [filterInput])

    const filteredItems = useMemo(() => {
        if (!items || !Array.isArray(items)) return []
        if (filterConditions.length === 0) return items

        return items.filter(worker => {
            if (!worker) return false

            return filterConditions.every(condition => {
                try {
                    const workerValue = getWorkerFieldValue(worker, condition.field)
                    return compareValues(workerValue, condition.operator, condition.value)
                } catch (error) {
                    console.warn('Error filtering worker:', error)
                    return false
                }
            })
        })
    }, [items, filterConditions])

    const sortedItems = useMemo(() => {
        if (!filteredItems) return []

        return [...filteredItems].sort((a, b) => {
            const aValue = getSortValue(a, sortConfig.field)
            const bValue = getSortValue(b, sortConfig.field)

            if (aValue == null) return 1
            if (bValue == null) return -1

            if (typeof aValue === 'string' && typeof bValue === 'string') {
                return sortConfig.direction === 'asc'
                    ? aValue.localeCompare(bValue)
                    : bValue.localeCompare(aValue)
            }

            if (aValue < bValue) return sortConfig.direction === 'asc' ? -1 : 1
            if (aValue > bValue) return sortConfig.direction === 'asc' ? 1 : -1
            return 0
        })
    }, [filteredItems, sortConfig])

    const paginatedItems = useMemo(() => {
        const startIndex = (pagination.currentPage - 1) * 12
        const endIndex = startIndex + 12
        return sortedItems.slice(startIndex, endIndex)
    }, [sortedItems, pagination])

    const totalPages = useMemo(() => {
        return Math.ceil(sortedItems.length / 12)
    }, [sortedItems.length])

    const handleSort = (field: SortField) => {
        setSortConfig(prevConfig => ({
            field,
            direction: prevConfig.field === field && prevConfig.direction === 'asc' ? 'desc' : 'asc'
        }))
    }

    const handleFilterChange = (value: string) => {
        setFilterInput(value)
        setPagination({currentPage: 1})
    }

    const handlePageChange = (page: number) => {
        setPagination({currentPage: page})
    }

    const clearFilter = () => {
        setFilterInput('')
        setPagination({currentPage: 1})
    }

    return {
        paginatedItems,
        sortedItems,
        filteredItems,

        sortConfig,
        filterInput,
        filterConditions,
        pagination,
        totalPages,

        handleSort,
        handleFilterChange,
        handlePageChange,
        clearFilter
    }
}

function getWorkerFieldValue(worker: Worker, field: string): any {
    const fieldMap: { [key: string]: (w: Worker) => any } = {
        'id': w => w.id,
        'name': w => w.name,
        'salary': w => w.salary,
        'rating': w => w.rating,
        'position': w => w.position,
        'status': w => w.status,
        'creationDate': w => w.creationDate,
        'startDate': w => w.startDate,
        'coordinates.x': w => w.coordinates?.x,
        'coordinates.y': w => w.coordinates?.y,
        'organization.turnover': w => w.organization?.annualTurnover,
        'organization.employees': w => w.organization?.employeesCount,
        'organization.rating': w => w.organization?.rating,
        'organization.type': w => w.organization?.organizationType,
        'person.eyeColor': w => w.person?.eyeColor,
        'person.hairColor': w => w.person?.hairColor,
        'person.passportID': w => w.person?.passportID,
        'person.nationality': w => w.person?.nationality,
        'person.location.x': w => w.person?.location?.x,
        'person.location.y': w => w.person?.location?.y,
        'person.location.z': w => w.person?.location?.z,
        'person.location.name': w => w.person?.location?.name
    }

    const getter = fieldMap[field]
    return getter ? getter(worker) : undefined
}

function compareValues(workerValue: any, operator: FilterOperator, filterValue: string): boolean {
    if (workerValue == null) return false

    switch (operator) {
        case '=':
            return workerValue.toString() === filterValue
        case '>':
            if (typeof workerValue === 'number' && !isNaN(Number(filterValue))) {
                return workerValue > Number(filterValue)
            }
            return workerValue.toString() > filterValue
        case '<':
            if (typeof workerValue === 'number' && !isNaN(Number(filterValue))) {
                return workerValue < Number(filterValue)
            }
            return workerValue.toString() < filterValue
        case '~':
            return workerValue.toString().toLowerCase().includes(filterValue.toLowerCase())
        default:
            return false
    }
}

function getSortValue(worker: Worker, field: SortField): any {
    switch (field) {
        case 'coordinates':
            return worker.coordinates ? `${worker.coordinates.x},${worker.coordinates.y}` : ''
        case 'organization':
            return worker.organization?.id || ''
        case 'person':
            return worker.person?.passportID || ''
        default:
            return worker[field]
    }
}
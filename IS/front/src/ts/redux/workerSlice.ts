import {createSlice} from '@reduxjs/toolkit'
import Color from "../data/Color";
import Position from "../data/Position";
import Worker from "../data/Worker";
import Coordinates from "../data/Coordinates";
import Person from "../data/Person";

export const workerSlice = createSlice({
    name: 'items',
    initialState: {
        items: [],
        organizationRequired: false,
        locationRequired: false,
        workerView: new Worker(0, '', new Coordinates(0, 0), '', '', Position.DIRECTOR, new Person(Color.RED, null, '', null, null), null, null, null, null),
        updateMode: false,
        viewMode: true,
        updatable: true
    },
    reducers: {
        updateItems: (state, action) => {
            state.items = action.payload
        },
        updateOrganizationRequired: (state, action) => {
            state.organizationRequired = action.payload
        },
        updateLocationRequired: (state, action) => {
            state.locationRequired = action.payload
        },
        updateWorkerView: (state, action) => {
            state.workerView = action.payload
        },
        updateBuildMode: (state, action) => {
            state.updateMode = action.payload
        },
        updateViewMode: (state, action) => {
            state.viewMode = action.payload
        },
        setUpdatable: (state, action) => {
            state.updatable = action.payload
        }
    }
})

export const {
    updateViewMode,
    updateBuildMode,
    updateWorkerView,
    updateLocationRequired,
    updateOrganizationRequired,
    updateItems,
    setUpdatable
} = workerSlice.actions

export default workerSlice.reducer
import {createSlice} from '@reduxjs/toolkit'

export const dataSlice = createSlice({
    name: 'items',
    initialState: {
        x: '',
        y: '',
        r: '',
        items: []
    },
    reducers: {
        updateItems: (state, action) => {
            state.items = action.payload
        },
        updateX: (state, action) => {
            state.x = action.payload
        },
        updateY: (state, action) => {
            state.y = action.payload
        },
        updateR: (state, action) => {
            if (state.r === '') document.getElementById("svgError").innerHTML = ""
            state.r = action.payload
        }
    }
})

export const {updateItems, updateX, updateY, updateR} = dataSlice.actions

export default dataSlice.reducer
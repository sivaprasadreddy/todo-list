import * as actionTypes from "../actions/actionTypes";

const todos = (state = [], action) => {
    switch (action.type) {
        case actionTypes.RECEIVE_TODOS:
            return action.payload || [];

        case actionTypes.ADD_TODO:
            return [
                ...state,
                {
                    id: action.id,
                    text: action.text,
                    completed: false
                }
            ];
        case actionTypes.TOGGLE_TODO:
            return state.map(todo => (todo.id === action.id ? { ...todo, completed: !todo.completed } : todo));
        default:
            return state;
    }
};

export default todos;

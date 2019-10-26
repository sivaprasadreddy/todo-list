import axios from "./axios";
import * as actionTypes from "./actionTypes";

export function login(credentials) {
    return dispatch => {
        return axios.post("/api/auth/login", credentials);
    };
}

export function fetchAllTodos() {
    return dispatch => {
        return axios("/api/todos")
            .then(response => {
                return dispatch({
                    type: actionTypes.RECEIVE_TODOS,
                    payload: response.data
                });
            })
            .catch(e => console.log("error", e));
    };
}

export function createTodo(task) {
    const todo = { text: task, done: false };
    return dispatch => {
        return axios
            .post("/api/todos", todo)
            .then(response => {
                dispatch(fetchAllTodos());
                return dispatch({
                    type: actionTypes.CREATE_TODO_SUCCESS,
                    payload: response.data
                });
            })
            .catch(e => console.log("error", e));
    };
}

export function toggleTodo(todo) {
    const updatedTodo = Object.assign({}, todo, { done: !todo.done });
    return dispatch => {
        return axios
            .put("/api/todos/" + updatedTodo.id, updatedTodo)
            .then(response => {
                dispatch(fetchAllTodos());
                return dispatch({
                    type: actionTypes.CREATE_TODO_SUCCESS,
                    payload: response.data
                });
            })
            .catch(e => console.log("error", e));
    };
}

export const setVisibilityFilter = filter => ({
    type: "SET_VISIBILITY_FILTER",
    filter
});

export const VisibilityFilters = {
    SHOW_ALL: "SHOW_ALL",
    SHOW_COMPLETED: "SHOW_COMPLETED",
    SHOW_ACTIVE: "SHOW_ACTIVE"
};

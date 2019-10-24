import React from "react";
import PropTypes from "prop-types";
import Todo from "./Todo";

const TodoList = ({ todos, toggleTodo }) => (
    <table className="table table-bordered table-hover mt-5">
        <thead>
            <tr>
                <th scope="col">Tasks</th>
            </tr>
        </thead>
        <tbody data-testid="todos-body">
            {todos.map(todo => (
                <Todo key={todo.id} {...todo} onClick={() => toggleTodo(todo)} />
            ))}
        </tbody>
    </table>
);

TodoList.propTypes = {
    todos: PropTypes.arrayOf(
        PropTypes.shape({
            id: PropTypes.number.isRequired,
            done: PropTypes.bool.isRequired,
            text: PropTypes.string.isRequired
        }).isRequired
    ).isRequired,
    toggleTodo: PropTypes.func.isRequired
};

export default TodoList;

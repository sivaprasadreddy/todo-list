import React from "react";
import PropTypes from "prop-types";

const Todo = ({ onClick, done, text }) => (
    <tr
        className={done ? "table-success" : ""}
        onClick={onClick}
        style={{
            textDecoration: done ? "line-through" : "none"
        }}
    >
        <td>{text}</td>
    </tr>
);

Todo.propTypes = {
    onClick: PropTypes.func.isRequired,
    done: PropTypes.bool.isRequired,
    text: PropTypes.string.isRequired
};

export default Todo;

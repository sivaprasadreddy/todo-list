import React from "react";
import { connect } from "react-redux";
import { createTodo } from "../store/actions/actionCreators";

const AddTodo = ({ dispatch }) => {
    let input;

    return (
        <form
            className="form-inline"
            onSubmit={e => {
                e.preventDefault();
                if (!input.value.trim()) {
                    return;
                }
                dispatch(createTodo(input.value));
                input.value = "";
            }}
        >
            <div className="form-group col-md-8">
                <input type="text" className="form-control col-md-12" ref={node => (input = node)} />
            </div>
            <button type="submit" className="btn btn-primary">
                Add Todo
            </button>
        </form>
    );
};

export default connect()(AddTodo);

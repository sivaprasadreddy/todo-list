import React from "react";
import { connect } from "react-redux";
import { toggleTodo, VisibilityFilters, fetchAllTodos } from "../store/actions/actionCreators";
import TodoList from "../components/TodoList";

class VisibleTodoList extends React.Component {
    componentDidMount() {
        this.props.fetchAllTodos();
    }

    render() {
        return <TodoList todos={this.props.todos} toggleTodo={this.props.toggleTodo} />;
    }
}
const getVisibleTodos = (todos, filter) => {
    switch (filter) {
        case VisibilityFilters.SHOW_ALL:
            return todos;
        case VisibilityFilters.SHOW_COMPLETED:
            return todos.filter(t => t.done);
        case VisibilityFilters.SHOW_ACTIVE:
            return todos.filter(t => !t.done);
        default:
            throw new Error("Unknown filter: " + filter);
    }
};

const mapStateToProps = state => ({
    todos: getVisibleTodos(state.todos, state.visibilityFilter)
});

const mapDispatchToProps = dispatch => ({
    fetchAllTodos: () => dispatch(fetchAllTodos()),
    toggleTodo: id => dispatch(toggleTodo(id))
});

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(VisibleTodoList);

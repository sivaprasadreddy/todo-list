import React from "react";
import Footer from "./Footer";
import AddTodo from "../containers/AddTodo";
import VisibleTodoList from "../containers/VisibleTodoList";

class Home extends React.Component {
    render() {
        return (
            <>
                <AddTodo />
                <VisibleTodoList />
                <Footer />
            </>
        );
    }
}

export default Home;

import React from "react";
import Footer from "../components/Footer";
import AddTodo from "./AddTodo";
import VisibleTodoList from "./VisibleTodoList";

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

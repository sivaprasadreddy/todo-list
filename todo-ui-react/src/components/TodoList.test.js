import React from "react";
import { render } from "@testing-library/react";
import TodoList from "./TodoList";

test("render todos when <TodoList/> component is mounted and displays them", async () => {
    const todos = [{ id: 1, text: "todo1", done: false }, { id: 2, text: "todo2", done: true }];
    const { getByTestId } = render(<TodoList todos={todos} toggleTodo={() => {}} />);
    const listOfTodos = getByTestId("todos-body");
    expect(listOfTodos.children.length).toBe(2);
});

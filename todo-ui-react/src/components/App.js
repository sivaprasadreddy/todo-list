import React from "react";
import { Route, Switch } from "react-router-dom";
import Home from "../containers/Home";
import NavBar from "./NavBar";
import Login from "../containers/Login";

const App = () => (
    <div className="App">
        <NavBar />
        <main role="main" className="container">
            <Switch>
                <Route path="/login" component={Login} />
                <Route component={Home} />
            </Switch>
        </main>
    </div>
);

export default App;

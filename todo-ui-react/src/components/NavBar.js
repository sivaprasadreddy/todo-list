import React from "react";
import { NavLink } from "react-router-dom";
import { connect } from "react-redux";

class NavBar extends React.Component {
    logoutHandler = () => {
        localStorage.removeItem("access_token");
        window.location = "/login";
    };

    render() {
        let logoutBtn;
        if (this.props.loggedIn) {
            logoutBtn = (
                <ul className="navbar-nav">
                    <li className="nav-item">
                        <button className="btn btn-outline-success my-2 my-sm-0" onClick={this.logoutHandler}>
                            Logout
                        </button>
                    </li>
                </ul>
            );
        } else {
            logoutBtn = "";
        }
        return (
            <nav className="navbar navbar-expand-md navbar-dark fixed-top bg-dark">
                <NavLink className="navbar-brand" to="/">
                    TodoList
                </NavLink>
                <button
                    className="navbar-toggler"
                    type="button"
                    data-toggle="collapse"
                    data-target="#navbarCollapse"
                    aria-controls="navbarCollapse"
                    aria-expanded="false"
                    aria-label="Toggle navigation"
                >
                    <span className="navbar-toggler-icon"></span>
                </button>
                <div className="collapse navbar-collapse" id="navbarCollapse">
                    <ul className="navbar-nav mr-auto">
                        <li className="nav-item">
                            <NavLink className="nav-link" to="/">
                                Home <span className="sr-only">(current)</span>
                            </NavLink>
                        </li>
                    </ul>
                    {logoutBtn}
                </div>
            </nav>
        );
    }
}

const mapStateToProps = state => {
    console.log("state: ", state);
    return {
        loggedIn: localStorage.getItem("access_token") ? true : false
    };
};

const mapDispatchToProps = dispatch => ({});

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(NavBar);

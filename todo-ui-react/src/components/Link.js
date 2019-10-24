import React from "react";
import PropTypes from "prop-types";

const Link = ({ active, children, onClick }) => (
    <button
        className="btn btn-warning"
        onClick={onClick}
        disabled={active}
        style={{
            marginLeft: "4px"
        }}
    >
        {children}
    </button>
);

Link.propTypes = {
    active: PropTypes.bool.isRequired,
    children: PropTypes.node.isRequired,
    onClick: PropTypes.func.isRequired
};

export default Link;

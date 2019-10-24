import axios from "axios";

const instance = axios.create();
const localApiUrl = "http://localhost:8080";

let apiUrl = process.env.REACT_APP_API_BASE_URL;
console.log("REACT_APP_API_BASE_URL from env: " + apiUrl);
apiUrl = apiUrl || localApiUrl;
console.log("Effective REACT_APP_API_BASE_URL from env: " + apiUrl);
instance.defaults.baseURL = apiUrl;

export default instance;

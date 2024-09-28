import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import "./App.css";
import "bootstrap/dist/css/bootstrap.min.css";
import CreateUserForm from "./components/CreateUserForm";
import NavBar from "./components/NavBar";
import UserList from "./components/UserList";
import Users from "./components/Users";
import { UserListProvider } from "./context/UserContext";
import UserDetail from "./components/UserDetail";
import UpdateUserForm from "./components/UpdateUserForm";

function App() {
  return (
    <Router>
      <UserListProvider>
        <div className="container">
          <NavBar />
          <hr />
          <Routes>
            <Route path="/new" element={<CreateUserForm />} />
            <Route path="/" element={<Users />}>
              <Route index element={<UserList />} />
              <Route path=":id" element={<UserDetail />} />
              <Route path=":id/edit" element={<UpdateUserForm />} />
            </Route>
          </Routes>
        </div>
      </UserListProvider>
    </Router>
  );
}

export default App;

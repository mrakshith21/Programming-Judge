import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import ProblemsList from './pages/ProblemsList';
import ProblemDetail from './pages/ProblemDetail';
import Submissions from './pages/Submissions';
import SubmissionDetail from './pages/SubmissionDetail';
import Login from './pages/Login';
import Register from './pages/Register';
import ProtectedRoute from './components/ProtectedRoute';
import { AuthProvider } from './context/AuthContext';

function App() {
  return (
    <AuthProvider>
      <Router>
        <div className="min-h-screen bg-gray-50">
          <Navbar />
          <main>
            <Routes>
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />
              <Route path="/" element={
                <ProtectedRoute>
                  <ProblemsList />
                </ProtectedRoute>
              } />
              <Route path="/problem/:id" element={
                <ProtectedRoute>
                  <ProblemDetail />
                </ProtectedRoute>
              } />
              <Route path="/submissions" element={
                <ProtectedRoute>
                  <Submissions />
                </ProtectedRoute>
              } />
              <Route path="/submission/:id" element={
                <ProtectedRoute>
                  <SubmissionDetail />
                </ProtectedRoute>
              } />
            </Routes>
          </main>
        </div>
      </Router>
    </AuthProvider>
  );
}

export default App;

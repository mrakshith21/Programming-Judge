import React, { useState, useEffect, useRef } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../services/api';
import Editor from '@monaco-editor/react';
import { Upload, Send, FileCode } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

const ProblemDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();
  const [problem, setProblem] = useState(null);
  const [loading, setLoading] = useState(true);
  const [code, setCode] = useState('');
  const [language, setLanguage] = useState('PYTHON_3_13');
  const [submitting, setSubmitting] = useState(false);
  const fileInputRef = useRef(null);

  useEffect(() => {
    const fetchProblem = async () => {
      try {
        const response = await api.get(`/problems/${id}`);
        setProblem(response.data);
      } catch (error) {
        console.error('Error fetching problem:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchProblem();
  }, [id]);

  const handleFileUpload = (event) => {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (e) => {
        setCode(e.target.result);
      };
      reader.readAsText(file);
    }
  };

  const handleSubmit = async () => {
    if (!code.trim()) {
      alert('Please provide some code.');
      return;
    }

    setSubmitting(true);
    try {
      const submissionData = {
        problemId: parseInt(id),
        username: user.username,
        language: language,
      };

      const formData = new FormData();
      formData.append('submission', new Blob([JSON.stringify(submissionData)], { type: 'application/json' }));
      
      // Create a file from the code string
      const extension = language === 'PYTHON_3_13' ? 'py' : language === 'JAVA_21' ? 'java' : 'cpp';
      const file = new File([code], `solution.${extension}`, { type: 'text/plain' });
      formData.append('file', file);

      await api.post('/submissions', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });

      navigate('/submissions');
    } catch (error) {
      console.error('Error submitting code:', error);
      alert('Failed to submit code.');
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
      </div>
    );
  }

  if (!problem) {
    return <div className="text-center py-12 text-gray-500">Problem not found.</div>;
  }

  return (
    <div className="max-w-7xl mx-auto py-6 px-4 sm:px-6 lg:px-8">
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        {/* Problem Description */}
        <div className="bg-white shadow sm:rounded-lg p-6">
          <h1 className="text-2xl font-bold text-gray-900 mb-4">{problem.problemName}</h1>
          <div className="flex space-x-4 mb-6 text-sm text-gray-500">
            <span className="bg-gray-100 px-2 py-1 rounded">Time: {problem.timeLimit}s</span>
            <span className="bg-gray-100 px-2 py-1 rounded">Memory: {problem.memoryLimit}MB</span>
          </div>
          <div className="prose max-w-none text-gray-700 whitespace-pre-wrap">
            {problem.problemDescription?.replace(/\\n/g, '\n')}
          </div>
        </div>

        {/* Editor Section */}
        <div className="flex flex-col space-y-4">
          <div className="bg-white shadow sm:rounded-lg p-6">
            <div className="flex flex-wrap items-center justify-between gap-4 mb-4">
              <div className="flex items-center space-x-4">
                <select
                  value={language}
                  onChange={(e) => setLanguage(e.target.value)}
                  className="block w-40 pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md border"
                >
                  <option value="PYTHON_3_13">Python 3.13</option>
                  <option value="JAVA_21">Java 21</option>
                  <option value="CPLUSPLUS_17">C++ 17</option>
                </select>
              </div>
              <div className="flex space-x-2">
                <button
                  onClick={() => fileInputRef.current.click()}
                  className="inline-flex items-center px-3 py-2 border border-gray-300 shadow-sm text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                >
                  <Upload className="h-4 w-4 mr-2" />
                  Upload
                </button>
                <input
                  type="file"
                  ref={fileInputRef}
                  onChange={handleFileUpload}
                  className="hidden"
                />
              </div>
            </div>

            <div className="border rounded-md overflow-hidden h-[500px]">
              <Editor
                height="100%"
                language={language === 'PYTHON_3_13' ? 'python' : language === 'JAVA_21' ? 'java' : 'cpp'}
                theme="vs-light"
                value={code}
                onChange={(value) => setCode(value)}
                options={{
                  minimap: { enabled: false },
                  fontSize: 14,
                  scrollBeyondLastLine: false,
                }}
              />
            </div>

            <div className="mt-4 flex justify-end">
              <button
                onClick={handleSubmit}
                disabled={submitting}
                className={`inline-flex items-center px-6 py-3 border border-transparent text-base font-medium rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 ${
                  submitting ? 'opacity-50 cursor-not-allowed' : ''
                }`}
              >
                {submitting ? (
                  'Submitting...'
                ) : (
                  <>
                    <Send className="h-5 w-5 mr-2" />
                    Submit Solution
                  </>
                )}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProblemDetail;

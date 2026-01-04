import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import api from '../services/api';
import Editor from '@monaco-editor/react';
import { Clock, User, Code, CheckCircle2, XCircle, AlertCircle, FileWarning, ArrowLeft } from 'lucide-react';

const SubmissionDetail = () => {
  const { id } = useParams();
  const [submission, setSubmission] = useState(null);
  const [code, setCode] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchSubmissionData = async () => {
      try {
        const [submissionRes, codeRes] = await Promise.all([
          api.get(`/submissions/${id}`),
          api.get(`/submissions/${id}/code`)
        ]);
        setSubmission(submissionRes.data);
        setCode(codeRes.data);
      } catch (error) {
        console.error('Error fetching submission details:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchSubmissionData();
  }, [id]);

  const getVerdictBadge = (verdict) => {
    if (!verdict) {
      return (
        <span className="inline-flex items-center px-3 py-1 rounded-full text-sm font-medium bg-gray-100 text-gray-800">
          <AlertCircle className="w-4 h-4 mr-1" />
          Unknown
        </span>
      );
    }

    const upperVerdict = verdict.toUpperCase();

    if (upperVerdict === 'ACCEPTED') {
      return (
        <span className="inline-flex items-center px-3 py-1 rounded-full text-sm font-medium bg-green-100 text-green-800">
          <CheckCircle2 className="w-4 h-4 mr-1" />
          Accepted
        </span>
      );
    }

    if (upperVerdict === 'PENDING' || upperVerdict === 'RUNNING') {
      return (
        <span className="inline-flex items-center px-3 py-1 rounded-full text-sm font-medium bg-yellow-100 text-yellow-800 animate-pulse">
          <Clock className="w-4 h-4 mr-1" />
          {verdict}
        </span>
      );
    }

    let Icon = XCircle;
    if (upperVerdict.includes('COMPILATION')) {
      Icon = FileWarning;
    }

    return (
      <span className="inline-flex items-center px-3 py-1 rounded-full text-sm font-medium bg-red-100 text-red-800">
        <Icon className="w-4 h-4 mr-1" />
        {verdict}
      </span>
    );
  };

  const getLanguageMode = (lang) => {
    if (!lang) return 'python';
    const l = lang.toUpperCase();
    if (l.includes('PYTHON')) return 'python';
    if (l.includes('JAVA')) return 'java';
    if (l.includes('CPP') || l.includes('C++')) return 'cpp';
    return 'text';
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
      </div>
    );
  }

  if (!submission) {
    return (
      <div className="max-w-7xl mx-auto py-12 px-4 sm:px-6 lg:px-8 text-center">
        <h2 className="text-2xl font-bold text-gray-900">Submission not found</h2>
        <Link to="/submissions" className="mt-4 text-indigo-600 hover:text-indigo-500 flex items-center justify-center">
          <ArrowLeft className="w-4 h-4 mr-1" /> Back to Submissions
        </Link>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto py-6 px-4 sm:px-6 lg:px-8">
      <div className="mb-6">
        <Link to="/submissions" className="text-indigo-600 hover:text-indigo-500 flex items-center">
          <ArrowLeft className="w-4 h-4 mr-1" /> Back to Submissions
        </Link>
      </div>

      <div className="bg-white shadow overflow-hidden sm:rounded-lg mb-8">
        <div className="px-4 py-5 sm:px-6 flex justify-between items-center">
          <div>
            <h3 className="text-lg leading-6 font-medium text-gray-900">
              Submission #{submission.submissionId}
            </h3>
            <p className="mt-1 max-w-2xl text-sm text-gray-500">
              Details and source code
            </p>
          </div>
          <div>
            {getVerdictBadge(submission.verdict)}
          </div>
        </div>
        <div className="border-t border-gray-200">
          <dl>
            <div className="bg-gray-50 px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
              <dt className="text-sm font-medium text-gray-500 flex items-center">
                <FileWarning className="w-4 h-4 mr-2" /> Problem ID
              </dt>
              <dd className="mt-1 text-sm text-indigo-600 sm:mt-0 sm:col-span-2">
                <Link to={`/problem/${submission.problemId}`}>
                  Problem {submission.problemId}
                </Link>
              </dd>
            </div>
            <div className="bg-white px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
              <dt className="text-sm font-medium text-gray-500 flex items-center">
                <User className="w-4 h-4 mr-2" /> User
              </dt>
              <dd className="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                {submission.username}
              </dd>
            </div>
            <div className="bg-gray-50 px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
              <dt className="text-sm font-medium text-gray-500 flex items-center">
                <Code className="w-4 h-4 mr-2" /> Language
              </dt>
              <dd className="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                {submission.language}
              </dd>
            </div>
            <div className="bg-white px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
              <dt className="text-sm font-medium text-gray-500 flex items-center">
                <Clock className="w-4 h-4 mr-2" /> Submitted At
              </dt>
              <dd className="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                {new Date(submission.submitted).toLocaleString()}
              </dd>
            </div>
          </dl>
        </div>
      </div>

      <div className="bg-white shadow sm:rounded-lg overflow-hidden">
        <div className="px-4 py-5 sm:px-6 border-b border-gray-200">
          <h3 className="text-lg leading-6 font-medium text-gray-900 flex items-center">
            <Code className="w-5 h-5 mr-2" /> Source Code
          </h3>
        </div>
        <div className="h-[500px]">
          <Editor
            height="100%"
            language={getLanguageMode(submission.language)}
            value={code}
            theme="vs-dark"
            options={{
              readOnly: true,
              minimap: { enabled: false },
              fontSize: 14,
              scrollBeyondLastLine: false,
            }}
          />
        </div>
      </div>
    </div>
  );
};

export default SubmissionDetail;

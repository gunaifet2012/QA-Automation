import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate, Trend, Counter } from 'k6/metrics';
import { randomString } from 'https://jslib.k6.io/k6-utils/1.4.0/index.js';

const errorRate = new Rate('error_rate');
const createDuration = new Trend('employee_create_duration', true);
const fetchDuration = new Trend('employee_fetch_duration', true);
const createdCount = new Counter('employees_created');

export const options = {
  stages: [
    { duration: '20s', target: 3 },
    { duration: '1m', target: 5 },
    { duration: '20s', target: 0 },
  ],
  thresholds: {
    http_req_duration: ['p(95)<5000', 'p(99)<8000'],
    http_req_failed: ['rate<0.05'],
    error_rate: ['rate<0.05'],
    employee_create_duration: ['p(95)<5000'],
    employee_fetch_duration: ['p(90)<2000'],
  },
};

const BASE_URL = __ENV.BASE_URL || 'https://opensource-demo.orangehrmlive.com';
const API_BASE = `${BASE_URL}/web/index.php/api/v2`;

function getAuthToken() {
  const res = http.post(
    `${API_BASE}/auth/login`,
    JSON.stringify({ username: 'Admin', password: 'admin123' }),
    { headers: { 'Content-Type': 'application/json' } }
  );
  if (res.status === 200) {
    return JSON.parse(res.body).data.token;
  }
  return null;
}

export function setup() {
  return { token: getAuthToken() };
}

export default function (data) {
  const token = data.token;
  if (!token) {
    errorRate.add(1);
    return;
  }

  const authHeaders = {
    'Content-Type': 'application/json',
    Authorization: `Bearer ${token}`,
  };

  // ── Test 1: Fetch employee list ───────────────────────────────────────────
  const fetchStart = Date.now();
  const listResponse = http.get(`${API_BASE}/pim/employees?limit=10&offset=0`, {
    headers: authHeaders,
  });
  fetchDuration.add(Date.now() - fetchStart);

  const fetchOk = check(listResponse, {
    'GET employees status 200': (r) => r.status === 200,
    'GET employees has data': (r) => {
      try { return JSON.parse(r.body).data !== undefined; } catch { return false; }
    },
    'GET employees < 2s': (r) => r.timings.duration < 2000,
  });
  errorRate.add(!fetchOk);

  sleep(0.5);

  // ── Test 2: Create an employee ────────────────────────────────────────────
  const firstName = 'K6' + randomString(5);
  const lastName = 'Perf' + randomString(5);

  const createPayload = JSON.stringify({ firstName, middleName: '', lastName });
  const createStart = Date.now();
  const createResponse = http.post(`${API_BASE}/pim/employees`, createPayload, {
    headers: authHeaders,
  });
  createDuration.add(Date.now() - createStart);

  const createOk = check(createResponse, {
    'POST employee status 200': (r) => r.status === 200,
    'POST employee has empNumber': (r) => {
      try { return JSON.parse(r.body).data.empNumber > 0; } catch { return false; }
    },
    'POST employee < 5s': (r) => r.timings.duration < 5000,
  });

  if (createOk) createdCount.add(1);
  errorRate.add(!createOk);

  sleep(1);
}

export function handleSummary(data) {
  return {
    'test-output/k6/employee-summary.json': JSON.stringify(data, null, 2),
  };
}

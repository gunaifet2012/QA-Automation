import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate, Trend, Counter } from 'k6/metrics';

const errorRate = new Rate('error_rate');
const loginDuration = new Trend('login_duration', true);
const loginSuccess = new Counter('login_success_count');

export const options = {
  stages: [
    { duration: '30s', target: 5 },   // ramp up to 5 users
    { duration: '1m', target: 10 },   // hold at 10 users
    { duration: '30s', target: 0 },   // ramp down
  ],
  thresholds: {
    http_req_duration: ['p(95)<3000', 'p(99)<5000'],
    http_req_failed: ['rate<0.05'],
    error_rate: ['rate<0.05'],
    login_duration: ['p(95)<3000'],
  },
};

const BASE_URL = __ENV.BASE_URL || 'https://opensource-demo.orangehrmlive.com';

export default function () {
  const loginPayload = JSON.stringify({
    username: 'Admin',
    password: 'admin123',
  });

  const headers = { 'Content-Type': 'application/json' };

  const startTime = Date.now();
  const response = http.post(
    `${BASE_URL}/web/index.php/api/v2/auth/login`,
    loginPayload,
    { headers }
  );
  const duration = Date.now() - startTime;

  loginDuration.add(duration);

  const success = check(response, {
    'status is 200': (r) => r.status === 200,
    'response has token': (r) => {
      try {
        const body = JSON.parse(r.body);
        return body.data && body.data.token && body.data.token.length > 0;
      } catch {
        return false;
      }
    },
    'response time < 3s': (r) => r.timings.duration < 3000,
  });

  errorRate.add(!success);
  if (success) loginSuccess.add(1);

  sleep(1);
}

export function handleSummary(data) {
  return {
    'test-output/k6/login-summary.json': JSON.stringify(data, null, 2),
  };
}

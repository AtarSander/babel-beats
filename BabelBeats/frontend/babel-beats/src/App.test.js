import { render, screen } from '@testing-library/react';
import LoginApp from './LoginApp';

test('renders learn react link', () => {
  render(<LoginApp />);
  const linkElement = screen.getByText(/learn react/i);
  expect(linkElement).toBeInTheDocument();
});

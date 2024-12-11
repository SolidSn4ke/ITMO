import { render, screen } from '@testing-library/react';
import App from './react_components/home/Home';

test('renders learn react link', () => {
  render(<App />);
  const linkElement = screen.getByText(/learn react/i);
  expect(linkElement).toBeInTheDocument();
});

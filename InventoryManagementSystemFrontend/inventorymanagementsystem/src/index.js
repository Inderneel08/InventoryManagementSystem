import React, { useEffect } from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import reportWebVitals from './reportWebVitals';

// const handleBeforeUnload = () => {
//   localStorage.removeItem('token');
// };

// function useBeforeUnloadEffect() {
//   useEffect(() => {
//     window.addEventListener('beforeunload', handleBeforeUnload);

//     return () => {
//       window.removeEventListener('beforeunload', handleBeforeUnload);
//     };
//   }, []);
// }

function Root() {
  // useBeforeUnloadEffect();

  return <App />;
}

ReactDOM.createRoot(document.getElementById('root')).render(<Root />);

reportWebVitals();

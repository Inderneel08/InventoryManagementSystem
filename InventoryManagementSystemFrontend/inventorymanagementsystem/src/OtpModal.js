import React, { useEffect, useState } from 'react';
import OtpInput from 'react-otp-input';
import { Modal, Button } from 'react-bootstrap';

export default function OtpModal() {
  const [otp, setOtp] = useState('');
  const [showModal, setShowModal] = useState(false);

  const handleClose = () => {
    setOtp('');
    setShowModal(false);
  };

  useEffect(() => {
    setShowModal(true);
  }, []);

  const otpInputStyle = {
    width: '50px', // Adjust width and height to make the input square
    height: '50px',
    margin: '0 5px',
    fontSize: '24px',
    borderRadius: '5px',
    border: '1px solid #ccc',
    textAlign: 'center',
  };

  const renderInput = (inputProps, index) => (
    <input {...inputProps} key={index} style={otpInputStyle} />
  );

  return (
    <Modal show={showModal} onHide={handleClose} backdrop="static" keyboard={false}>
      <Modal.Header closeButton>
        <Modal.Title>Enter OTP</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <div style={{ display: 'flex', justifyContent: 'center', padding: '20px' }}>
          <OtpInput
            value={otp}
            onChange={setOtp}
            numInputs={6}
            separator={<span />} // Use an empty span for separator
            isInputNum={true} // If your OTP consists only of numbers
            inputStyle={otpInputStyle}
          renderInput={renderInput}
          />
        </div>
      </Modal.Body>
      <Modal.Footer>
        <div className="d-flex justify-content-between w-100">
          <Button variant="primary">Submit</Button>
          <Button variant="secondary" onClick={handleClose}>Close</Button>
        </div>
      </Modal.Footer>
    </Modal>
  );
}

import {useEffect, useState} from 'react';

export const useModal = (initialState = false) => {
    const [isOpen, setIsOpen] = useState(initialState);

    useEffect(() => {
        if (isOpen) {
            document.body.classList.add('modal-open');
        } else {
            document.body.classList.remove('modal-open');
        }

        return () => {
            document.body.classList.remove('modal-open');
        };
    }, [isOpen]);

    const open = () => setIsOpen(true);
    const close = () => setIsOpen(false);
    const toggle = () => setIsOpen(!isOpen);

    return {
        isOpen,
        open,
        close,
        toggle
    };
};
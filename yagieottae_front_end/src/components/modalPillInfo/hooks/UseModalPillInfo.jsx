const { useState } = require("react");

export default function UseModalPillInfo()
{
    const [showModal, setShowModal] = useState(false);

    return ({
        showModal,
        setShowModal
    })
}
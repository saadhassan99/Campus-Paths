import React from 'react'
import './Header.css'

function Header() {
    return <div className={"header"}>
        <div className={"uwLogo"}>
            <img
                src={"https://s3-us-west-2.amazonaws.com/uw-s3-cdn/wp-content/uploads/sites/98/2017/06/07212347/BoundlessBand_LeftAngleWordmarkW-CMYK.png"} alt={"uwLogo"}/>
        </div>
    </div>
}

export default Header;
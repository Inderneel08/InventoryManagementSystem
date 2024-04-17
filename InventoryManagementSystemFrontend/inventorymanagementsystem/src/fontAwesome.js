// Import necessary FontAwesome components

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { library } from '@fortawesome/fontawesome-svg-core';
import { fab } from '@fortawesome/free-brands-svg-icons';
import { faGoogle, faFacebook } from '@fortawesome/free-brands-svg-icons';

// Add the brand icons to the library
library.add(fab, faGoogle, faFacebook);

export { faGoogle, faFacebook };
import React from 'react';
import Rating from '@material-ui/lab/Rating';
import Typography from '@material-ui/core/Typography';
import Box from '@material-ui/core/Box';


const BotRating = () => {
    
  const [value, setValue] = React.useState(0);

 

  return (
    <div>
      <Box component="fieldset" mb={3} borderColor="transparent">
        <Typography component="legend" variant="h6">How did I do?</Typography>
        <Rating
          name="simple-controlled"
          size={'large'}
          value={value}
          onChange={(event, newValue) => {
            setValue(newValue);
          }}
        />
      </Box>
     </div>
  );
}

export default BotRating;
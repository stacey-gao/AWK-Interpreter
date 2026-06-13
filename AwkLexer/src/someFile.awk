#THIS FILE IS NOT MINE, I ONLY USED FOR TESTING MY PROGRAM

#!/bin/awk -f

# This AWK script reads a CSV file and prints the values in the second column.

BEGIN {
    FS = ","  # Set the field separator to comma
}

{
    print $2  # Print the second column
}

{ p
}
#!/bin/bash
# First replace the old MegaDealCard in MegaDealsScreen.kt
sed -i '/@Composable/,/^}/d' app/src/main/java/com/example/ui/screens/MegaDealsScreen.kt

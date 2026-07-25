#!/bin/bash
sed -i '/@Composable/,/^}$/d' app/src/main/java/com/example/ui/screens/CircleDealsScreen.kt

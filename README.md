# XBAR Plugins

A repository to store my xbar-plugins. XBAR helps me customize my macOS menu
bar. The [XBAR repo](https://github.com/matryer/xbar) contains install
instructions, links to a plugin directory and a guide to write your own plugins.

My XBAR plugins require one dependency (apart from running on a mac and having
XBAR installed): [Babashka](https://github.com/babashka/babashka#installation)

These scripts should be copied to the XBAR plugin folder. Most likely to be
found here: `~/Library/Application Support/xbar/plugins`

Make sure the script has a name like `script-name.4h.clj` where the `.4h.` is
important because that's an interface to XBAR telling it the script needs to run
every 4 hours.

## Caffeinate

Decide if you want your system to fall asleep or stay awake independent of the
screen saver - and energy saver settings. When the 🥱 icon is in the menu bar,
the screen saver will work as usual. But when the ☕️ icon is in the menu bar,
your screen saver will not start. If you manually trigger the screen saver, the
system also won't fall asleep after the amount of time configured in the "energy
saver" settings of your OS. You've caffeinated your mac.

## Weeknr

Simply display the current week number and have a quick link to an online
calendar with more detailed calendar info.

## Pomodoro

This code helped me write my own xbar plugins in babashka:
https://github.com/alekseysotnikov/pomodoro-xbar

# Contributing

These are my own plugins. I don't want contributions or feature requests. If you
spot an obvious mistake or see an opportunity to improve the current
implementation (while preserving the exact same behaviour) I would appreciate it
if you'd create an issue.

# License

These plugins are free to use, copy and change at your own risk.

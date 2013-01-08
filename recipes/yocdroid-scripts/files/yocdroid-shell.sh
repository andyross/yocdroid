# Grab a one minute wake lock for each interactive command by stuff it
# into the shell prompt.  Device suspend while you're typing is hugely
# annoying.
if [ -w /sys/power/wake_lock ]; then
    PS1="\$(echo shell-prompt 60000000 > /sys/power/wake_lock)$PS1"
fi

alias ls='ls --color=auto'
alias grep='grep --color=auto'
alias egrep='egrep --color=auto'
alias fgrep='fgrep --color=auto'

shopt -s checkwinsize
shopt -s histappend
HISTCONTROL=ignoredups:ignorespace

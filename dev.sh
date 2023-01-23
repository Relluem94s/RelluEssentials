#!/bin/bash

###########################################################################################
#                                    Start DEV Environment                                #
#                                      by Relluem94 2023                                  #
###########################################################################################
#    
# 
 
tmux new-session -d -s bash-session 'bash';
tmux send 'cd ~/repos/test-server && clear' ENTER;
tmux select-pane -T 'Minecraft Server'
tmux send 'docker start mysql' ENTER;
tmux send 'docker start phpmyadmin' ENTER;
tmux send 'java -jar spigot-1.19.3.jar -nogui' ENTER;


tmux split-window -h;
tmux select-pane -T 'Maven'
tmux send 'cd ~/repos/RelluBash-Script-Collection && clear' ENTER;


tmux split-window -v;
tmux select-pane -T 'GIT'
tmux send 'cd ~/repos/RelluEssentials && clear' ENTER;
tmux a;  



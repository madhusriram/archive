library("lme4")
library("psych")
 
data <- read.csv("neuralout.csv", header=T)

# omit trials without choice
data <- subset(data, ch1>0 & ch2 > 0) 
len <- nrow(data)

# recode the variables of interest
data$mn <- 2*data$mn-1
data$rare <- 2 * as(data$ch1 != (data$st-1),"numeric") - 1

# dependent variable: stay/switch
# whether the subsequent ch1 was a switch rel to current
data$stay <- c(data$ch1[2:len] == data$ch1[1:len-1],NaN) 

#rewarded, common
rewcom <- subset(data, mn == 1 & rare == -1)
rewcom <- na.omit(rewcom)
describe(rewcom$stay)

#rewarded, rare
rewrare <- subset(data, mn == 1 & rare == 1)
rewrare <- na.omit(rewrare)
describe(rewrare$stay)

#unrewarded, common
unrewcom <- subset(data, mn == -1 & rare == -1)
unrewcom <- na.omit(unrewcom)
describe(unrewcom$stay)

#unrewarded, rare
unrewrare <- subset(data, mn == -1 & rare == 1)
unrewrare <- na.omit(unrewrare)
describe(unrewrare$stay)


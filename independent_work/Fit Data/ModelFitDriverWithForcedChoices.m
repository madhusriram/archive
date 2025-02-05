function [BestFit hessian] = ModelFitDriverWithForcedChoices(CSs, Rew, is_instrumental, numiter)

Nsubjects=1;
Ntrials = length(CSs);

% to change between 4 and 5 parms, need to remove/add x(5) after the init, Kappa
% statements, and ALSO add/remove Kappa from the derivative in TDfitBehavior
NBeta = 1; % how many betas will we use (to prevent having to change all the code below every time we change from 3 to 1)
Fit = {}; 
clear Eta Beta Kappa
for iter = 1:numiter;   % run 5 times from random initial conditions, to get best fit
    for i = 1:Nsubjects;
        fprintf('%d...',i)
        % loading the test data of this subject
        C = CSs(i,:);
        R = Rew(i,:); % it turns out that it is better to scale rewards to the range of 0-4; they are already in that range here
        I = is_instrumental(i,:);
        LB = [1e-6 (1e-6)*ones(1,NBeta)];% 0+(1e-6)];
        UB = [1-(1e-6) 30*ones(1,NBeta)];% 1-(1e-6)];
        init = rand(1,length(LB)).*(UB-LB)+LB;% random initialization

        [res,lik,exitflag,output,lambda,grad,hessian] = ... 
            fmincon(@(x) loglike_bandit_forced(C,R,Ntrials,I,x(1),x(2:2+(NBeta-1)),0), init,[],[],[],[],LB,UB,[],...
            optimset('maxfunevals',20000,'maxiter',5000,'GradObj','on','DerivativeCheck','off','LargeScale','off','Algorithm','active-set'));
        % GradObj = 'on' to use gradients, 'off' to not use them
        % DerivativeCheck = 'on' to have fminsearch compute derivatives numerically and check the ones I supply
        % LargeScale = 'on' to use large scale methods, 'off' to use medium
        Eta(i) = res(1); 
        Beta(i,:) = res(2:2+(NBeta-1)); 
        % Kappa(i) = res(2+NBeta); 
        
        % once more, with feeling (to make sure we have the likelihood of the best
        % parameters)
        % [lik,dummy] = FitRiskBehaviorExercise(T,C,R,Eta(i),Beta(i,:),Kappa(i));        
        % [lik,dummy] = FitRiskBehaviorExercise(T,C,R,Eta(i),Beta(i,:),0);
        Lik(i) = lik;
        Hess{i} = hessian;
    end
    fprintf('\n')
    % print out the results
    % Fit{iter} = [[1:Nsubjects]' Eta' Beta Kappa' Lik'];
    Fit{iter} = [[1:Nsubjects]' Eta' Beta Lik' Hess'];
    L(:,iter) = Lik';  % Check this to see the likelihoods from the different runs (to check how stable the fits were to different starting points)
end
clear BestFit
[a,b] = min(L');
for i = 1:Nsubjects
    BestFit(i,:) = Fit{b(i)}(i,:);
end
% print the results
%fprintf('Subject\t eta\t beta\t likelihood\n')
%fprintf('%d\t %3.4f\t %3.4f\t %3.4f\n',BestFit')
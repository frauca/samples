function [C, sigma] = dataset3Params(X, y, Xval, yval)
%DATASET3PARAMS returns your choice of C and sigma for Part 3 of the exercise
%where you select the optimal (C, sigma) learning parameters to use for SVM
%with RBF kernel
%   [C, sigma] = DATASET3PARAMS(X, y, Xval, yval) returns your choice of C and 
%   sigma. You should complete this function to return the optimal C and 
%   sigma based on a cross-validation set.
%

% You need to return the following variables correctly.
C = 1;
sigma = 0.1;
return

% ====================== YOUR CODE HERE ======================
% Instructions: Fill in this function to return the optimal C and sigma
%               learning parameters found using the cross validation set.
%               You can use svmPredict to predict the labels on the cross
%               validation set. For example, 
%                   predictions = svmPredict(model, Xval);
%               will return the predictions on the cross validation set.
%
%  Note: You can compute the prediction error using 
%        mean(double(predictions ~= yval))
%
posssibilities=[0.01, 0.03, 0.1, 0.3, 1, 3, 10, 30];
[posa,posb] = meshgrid(posssibilities, posssibilities);
pairs = [posa(:) posb(:)];

costs=zeros(1,size(pairs));

function [a,b] = vect2Var(x)
  a=x(1);
  b=x(2);
end

for i = 1:size(pairs)
  [posC,posSigma] = vect2Var(pairs(i,:))
  model = svmTrain(X,y,posC,@(x1, x2) gaussianKernel(x1, x2, posSigma));
  predictions = svmPredict(model, Xval);
  costs(i) = mean(double(predictions ~= yval));
endfor

[minval,pairrow] = min(costs);
[C,sigma] = vect2Var(pairs(pairrow,:))

% =========================================================================

end

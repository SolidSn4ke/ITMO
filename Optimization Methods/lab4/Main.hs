import Text.Printf

coordDescent::(Double -> Double -> Double) -> (Double, Double) -> Double -> IO()
coordDescent f start precision = putStrLn $ helper start
    where
        helper (x10,x20) | abs ((f x11 x21) - (f x10 x20)) < precision = printf "Найденный минимум %f\nТочка минимума: (%s;%s)" (f x11 x21) (show x11) (show x21)
                         | otherwise = helper (x11, x21)
            where
                f1 = \x -> f x x20
                f2 = \x -> f x11 x
                x11 = bisection f1 (x10 - 100) (x10 + 100) precision
                x21 = bisection f2 (x20 - 100) (x20 + 100) precision


gradDescent::(Double -> Double -> Double) -> (Double, Double) -> Double -> IO()
gradDescent f start precision = putStrLn $ helper start 1
    where
        helper (x10,x20) lambda | abs ((f x11 x21) - (f x10 x20)) < precision = printf "Найденный минимум %f\nТочка минимума: (%s;%s)" (f x11 x21) (show x11) (show x21)
                                | (f x11 x21) >=  (f x10 x20) = helper (x10,x20) (lambda/2)
                                | otherwise = helper (x11, x21) lambda
            where
                f1 = \x -> f x x20
                f2 = \x -> f x10 x
                gradf1 = derivative f1
                gradf2 = derivative f2
                x11 = x10 - lambda * gradf1 x10
                x21 = x20 - lambda * gradf2 x20


steepDescent::(Double -> Double -> Double) -> (Double, Double) -> Double -> IO()
steepDescent f start precision = putStrLn $ helper start
    where
        helper (x10,x20) | sqrt ((gradf1 x10)^2 + (gradf2 x20)^2) < precision = printf "Найденный минимум %f\nТочка минимума: (%s;%s)" (f x10 x20) (show x10) (show x20)
                         | otherwise = helper (x11 h, x21 h)
            where
                f1 = \x -> f x x20
                f2 = \x -> f x10 x
                gradf1 = derivative f1
                gradf2 = derivative f2
                x11 = \h -> x10 - h * gradf1 x10 
                x21 = \h -> x20 - h * gradf2 x20
                f_h = \h -> f (x11 h) (x21 h)
                h = bisection f_h (-1000) (1000) precision


bisection :: (Double -> Double) -> Double -> Double -> Double -> Double
bisection f a b e = helper a b 0
    where 
        helper a b iter | (b - a) <= 2 * e = xm
                        | f x1 > f x2 = helper x1 b (iter+1)
                        | otherwise = helper a x2 (iter+1)
            where
                x1 = (a + b - e) / 2
                x2 = (a + b + e) / 2
                xm = ((a + b) / 2)
                

derivative :: (Double -> Double) -> (Double -> Double)
derivative f = \x -> (f (x + 0.0000001) - f x) / 0.0000001
    

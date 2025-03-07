import Text.Printf
import Data.List

bisection :: (Double -> Double) -> Double -> Double -> Double -> IO ()
bisection f a b e = putStrLn $ helper a b 0
    where 
        helper a b iter | (b - a) <= 2 * e = printf  "x = %f\nf(%f) = %f\nКоличество итераций: %s" xm xm (f xm) (show iter)
                        | f x1 > f x2 = helper x1 b (iter+1)
                        | otherwise = helper a x2 (iter+1)
            where
                x1 = (a + b - e) / 2
                x2 = (a + b + e) / 2
                xm = ((a + b) / 2)
        

goldenRatio :: (Double -> Double) -> Double -> Double -> Double -> IO ()
goldenRatio f a b e = putStrLn $ helper (a, b) (0, 0) 0 0
    where
        helper (a, b) (prev_a, prev_b) prev_x iter | b - a < e = printf "x = %f\nf(%f) = %f\nКоличество итераций: %s" x x (f x) (show iter) 
                                                   | f x1 < f x2 = helper (a, x2) (a, b) x1 (iter + 1)
                                                   | otherwise = helper (x1, b) (a, b) x2 (iter + 1)
            where
                x1 = if prev_a == prev_b || a == prev_a then a + 0.382 * (b - a) else prev_x
                x2 = if prev_a == prev_b || b == prev_b then a + 0.618 * (b - a) else prev_x
                x = ((a + b) / 2)


chord :: (Double -> Double) -> Double -> Double -> Double -> IO ()
chord f a b e = putStrLn $ helper a b 0
    where 
        helper a b iter | abs (f' x) < e = printf "x = %f\nf(%f) = %f\nКоличество итераций: %s" x x (f x) (show iter)
                        | f' x > 0 = helper a x (iter + 1)
                        | otherwise = helper x b (iter + 1)
            where
                x = a - (f' a) / (f' a - f' b) * (a - b)
                f' = derivative f


newton :: (Double -> Double) -> Double -> Double -> Double -> IO ()
newton f a b e = putStrLn $ if (f' a) * (f''' a) > 0 then helper a 0 else helper b 0
    where
        helper x iter | abs (f' x) < e = printf "x = %f\nf(%f) = %f\nКоличество итераций: %s" x x (f x) (show iter)
                      | otherwise = helper xk (iter + 1)
            where
                xk = x - (f' x) / (f'' x)
        f' = derivative f
        f'' = derivative f'
        f''' = derivative f''


squareApprox :: (Double -> Double) -> Double -> Double -> Double -> Double -> IO ()
squareApprox f x1 delta e1 e2 = putStrLn $ helper x1 (0,0,0) 0
    where  
        helper x1 (prev_x1, prev, prev_x3) iter | abs ((f_min - f x) / f x) < e1 && abs ((x_min - x) / x) < e2 = printf "x = %f\nf(%f) = %f\nКоличество итераций: %s" x x (f x) (show iter)
                       | x >= x1 && x <= x3 = helper (min x1 x3) (min x1 x3, min x_min x, min (max x_min x) x2) (iter + 1)
                       | otherwise = helper x (0,0,0) (iter + 1)
            where
                x2 = if prev_x1 == prev && prev == prev_x3 && prev_x3 == 0 then x1 + delta else prev
                x3 = if prev_x1 == prev && prev == prev_x3 && prev_x3 == 0 then (if f x1 > f x2 then x1 + 2 * delta else x1 - delta) else prev_x3
                x = 0.5 * ((x2^2 - x3^2) * (f x1) + (x3^2 - x1^2) * (f x2) + (x1^2 - x2^2) * (f x3)) / ((x2 - x3) * (f x1) + (x3 - x1) * (f x2) + (x1 - x2) * (f x3))
                x_min = snd $ head $ sortBy (\(a,_) (b,_) -> compare a b) [(f x1, x1), (f x2, x2), (f x3, x3)]
                f_min = head $ sort [(f x1), (f x2), (f x3)]


derivative :: (Double -> Double) -> (Double -> Double)
derivative f = \x -> (f (x + 0.0000001) - f x) / 0.0000001
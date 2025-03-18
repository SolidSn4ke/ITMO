import Text.Printf
import Data.List

data IntegType = LeftRec | CenterRec | RightRec | Trapezoid | Simpson deriving (Enum, Eq)

integrate::(Double -> Double) -> IntegType -> Double -> Double -> Double -> IO()
integrate f integType a b precision = putStrLn $ (helper 4 [(2, calc 2)])
    where
        k | (integType == LeftRec || integType ==  RightRec) = 1
          | (integType == CenterRec || integType == Trapezoid) = 2
          | integType == Simpson = 4
        step n = (b - a) / n
        helper n pairs | abs (calc n - calc (n / 2))/(2^k - 1) < precision = (printf "\nНайденное значение интеграла: %f\nЧисло отрезков в разбиении интервала: %s\nПравило Рунге: %f\n" (calc n) ((show . round) n) (abs (calc n - calc (n / 2))/(2^k - 1))) ++ (concatMap (\(n, val) -> printf "n = %s => integ = %s => R = %f\n" (show n) (show val) (abs (calc n - calc (n / 2))/(2^k - 1))) pairs)
                       | otherwise = helper (n * 2) ((n, calc n) : pairs)
        calc n | integType == LeftRec = step n * (sum . map f ) [a, a + step n..b - step n]
               | integType == CenterRec = step n * (sum . map f) [a + 0.5 * (step n), a + 1.5 * (step n)..b - 0.5 * (step n)]
               | integType == RightRec = step n * (sum . map f) [a + step n, a + 2 * (step n)..b]
               | integType == Trapezoid = step n * ((f a + f b) / 2 + (sum . map f) [a + step n, a + 2 * step n..b - step n])
               | integType == Simpson = (step n) / 3 * (f a + f b + 4 * (sum . map f) [a+step n,a+3*step n..b - step n] + 2 * (sum . map f) [a+2*step n,a+4*step n..b-2*step n])

(ns tween-clj.core-test
  (:use clojure.test
        tween-clj.core))

(defn almost= 
  "checks if two numbers are almost the same (to allow for fix point number wierdness)"
  [^double val1 ^double val2]
  (< (Math/abs (- val1 val2)) (double 0.00000001)))

(deftest linear-tween
  (testing "linear tween just returns back same value even if eased-in"
    (are [p p'] (almost= (ease-in transition-linear p) p')
         (double 0)    (double 0)
         (double 0.1)  (double 0.1)
         (double 0.5)  (double 0.5)
         (double 0.9)  (double 0.9)
         (double 1)    (double 1)
         ))

  (testing "linear tween just returns back same value even if eased-out"
    (are [p p'] (almost= (ease-out transition-linear p) p')
         0    0
         0.1  0.1
         0.5  0.5
         0.9  0.9
         1    1
         ))

  (testing "linear tween just returns back same value even if eased-in-out"
    (are [p p'] (almost= (ease-in-out transition-linear p) p')
         0    0
         0.1  0.1
         0.5  0.5
         0.9  0.9
         1    1
         )))

(deftest circ-tween
  (testing "circ transition"
    (are [p p'] (almost= (ease-in transition-circ p) p')
         0    0.0
         0.1  0.005012562893380035
         0.2  0.020204102886728803
         0.5  0.1339745962155613
         0.9  0.5641101056459328
         1    1.0
         )))

(deftest back-tween
  (testing "back transition"
    (are [p p'] (almost= (ease-in transition-back p) p')
         0    -0.0
         0.1  -0.013562000000000003
         0.2  -0.04377600000000001
         0.5  -0.07724999999999999
         0.9  0.5979420000000002
         1    1.0000000000000002
         ))
  (testing "back transition with custom x"
    (are [p p'] (almost= (ease-in (partial transition-back 0.2) p) p')
         0    -0.0
         0.1  -8.000000000000004E-4
         0.2  0.0015999999999999994
         0.5  0.09999999999999999
         0.9  0.7128000000000001
         1    1.0
         )))

(deftest elastic-tween
  (testing "elastic transition"
    (are [p p'] (almost= (ease-in transition-elastic p) p')
         0    9.765625E-4
         0.1  -9.765624999999996E-4
         0.2  -0.0019531250000000017
         0.5  -0.01562500000000004
         0.9  0.5000000000000001
         1    -0.4999999999999972)))

(deftest exponential-tween
  (testing "exponential transition"
    (are [p p'] (almost= (ease-out transition-expo p) p')
         0    0.0
         0.1  0.42565082250148245
         0.2  0.6701230223067763
         0.5  0.9375
         0.9  0.9931988237242491
         1    0.99609375)))

(deftest sine-tween
  (testing "sine transition"
    (are [p p'] (almost= (ease-in transition-sine p) p')
         0    0.0
         0.1  0.01231165940486223
         0.2  0.04894348370484647
         0.5  0.29289321881345254
         0.9  0.8435655349597692
         1    1.0
         )))

(deftest bounce-tween
  (testing "bounce transition"
    (are [p p'] (almost= (ease-in transition-bounce p) p')
         0    0.0
         0.1  0.01187500000000001
         0.2  0.06
         0.5  0.234375
         0.9  0.9243750000000001
         1    1.0
         )))

(deftest pow-tween
  (testing "eased in pow transition moves quickly towards the end"
    (are [p p'] (almost= (ease-in transition-pow p) p')
         0    0
         0.1  0.000001
         0.2  0.000064
         0.5  0.015625
         0.9  0.531441
         1    1
         ))
  (testing "ease out pow transition moves quickly from the start"
    (are [p p'] (almost= (ease-out transition-pow p) p')
         0    0
         0.1  0.46855899999999995
         0.2  0.7378559999999998
         0.5  0.984375
         0.9  0.999999
         1    1
         ))

  (testing "pow transition with custom power"
    (are [p p'] (almost= (ease-out (partial transition-pow 2) p) p')
         0    0.0
         0.1  0.18999999999999995
         0.2  0.3599999999999999
         0.5  0.75
         0.9  0.99
         1    1.0
         )))

(deftest test-range-to-p
  
  (is (= (range-to-p 0 10 3) 0.3))
  (is (= (range-to-p 100 110 109) 0.9))

  )

(comment
  (map (partial ease-in transition-elastic) [0 0.1 0.2 0.5 0.9 1])
  )
(ns clojure-processing-sketch-example.sketches
  (:import processing.core.PApplet
           ;; specific sketches
           Jukebox ExampleSketch RandomCircles MinimPlayPause))

;; keys should be [papplet time]
(def current-sketch
  (atom {}))

(defn touch-current-time
  "'touch' sketch with current time"
  []
  (swap! current-sketch assoc :time (System/currentTimeMillis)))

(defmacro sketch-proxy
  [applet-subclass]
  `(fn []
     (proxy [~applet-subclass] []
       (exitActual []))))

;; (defmacro sketch-proxy
;;   [applet-subclass]
;;   `(fn []
;;      (proxy [~applet-subclass] []
;;        (exitActual [])
;;        (mousePressed []
;;          (touch-current-time)
;;          (proxy-super mouseClicked))
;;        (mouseClicked []
;;          (touch-current-time)
;;          (proxy-super mouseClicked))
;;        (mouseDragged []
;;          (touch-current-time)
;;          (proxy-super mouseDragged))
;;        (mouseMoved []
;;          (touch-current-time)
;;          (proxy-super mouseMoved))
;;        (mousePressed []
;;          (touch-current-time)
;;          (proxy-super mousePressed))
;;        (mouseReleased []
;;          (touch-current-time)
;;          (proxy-super mouseReleased))
;;        (mouseWheel []
;;          (touch-current-time)
;;          (proxy-super mouseWheel))
;;        (keyPressed []
;;          (touch-current-time)
;;          (proxy-super keyPressed))
;;        (keyReleased []
;;          (touch-current-time)
;;          (proxy-super keyReleased))
;;        (keyTyped []
;;          (touch-current-time)
;;          (proxy-super keyTyped)))))

(def ^{:doc "the applet for the \"Jukebox\" sketch selection screen"}
  jukebox-sketch
  (proxy [Jukebox] []
    (exitActual [])
    (switchToSketch [uid]
      (println "recieved" uid)
      (proxy-super switchToSketch uid))))

(def sketches
  [{:uid "u1papplet111111" :constructor (sketch-proxy ExampleSketch) :weight 1}
   {:uid "u2papplet222222" :constructor (sketch-proxy RandomCircles) :weight 1}
   {:uid "u3papplet333333" :constructor (sketch-proxy MinimPlayPause) :weight 1}])

(defn set-current
  [papplet]
  (reset!
   current-sketch
   {:papplet papplet
    :time (System/currentTimeMillis)}))

(defn set-random
  [uid]
  (reset!
   current-sketch
   {:papplet (:constructor (rand-nth sketches))
    :time (System/currentTimeMillis)}))

(defn run [papplet]
  (PApplet/runSketch
      (into-array String ["mysketch" "--present"])
    papplet)
  papplet)

(defn stop
  ([papplet]
   (.exit papplet))
  ([papplet delay-ms]
   (Thread/sleep delay-ms)
   ;; probably should check it hasn't already been exited before
   ;; calling exit?
   (.exit papplet)))

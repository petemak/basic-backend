(ns pjm.backend.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.defaults :as defaults]))

;; Atom holds sewrver reference
(def server (atom nil))

;; Basic handler

(defn handler
  "HTTP 200 handler. just returns HTTP 200 OK"
  [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body    (str "OK" (:remote-addr request))})

(defn start-server!
  "Starts server and stores a reference in
  the server atom. Does not block main thread"
  []
  (swap! server
         (fn [_]
           (jetty/run-jetty (defaults/wrap-defaults #'handler defaults/site-defaults)
                            {:port 8000
                             :join? false}))))

(defn stop-server!
  "Checks if the server is running and then calls .stop
   jetty/run-jetty returns an object that represents the server.
  That object has a .stop method on it. "
  []
  (when-some [s @server]
    (.stop s)
    (reset! server nil)))


(defn -main
  [& args]
 )

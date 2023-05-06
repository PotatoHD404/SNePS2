(defpackage :snepslog-helper
  (:use :common-lisp)
;;  (:import-from :snepslog :tell)
;;  (:import-from :sneps :show)
  (:export :run-demo))

(in-package :snepslog-helper)

(defun run-demo ()
    (snepslog:tell "(\~p <=> q) <=> (p <=> ~q)?")
    (sneps:show))
package errors

type AppError struct {
	Code    int    `json:"code"`
	Message string `json:"message"`
}

func NewAppError(code int, message string) *AppError {
	return &AppError{
		Code:    code,
		Message: message,
	}
}

func (e *AppError) Error() string {
	return e.Message
}

func (e *AppError) StatusCode() int {
	return e.Code
}

func (e *AppError) Response() map[string]interface{} {
	return map[string]interface{}{
		"error": map[string]interface{}{
			"code":    e.Code,
			"message": e.Message,
		},
	}
}
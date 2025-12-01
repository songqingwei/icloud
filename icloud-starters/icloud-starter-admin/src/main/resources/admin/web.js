(function (win) {
  const { RenderEngine, getInitState, getInitAppLifeCycles } = win;
  const { registerComponent } = RenderEngine;
  const COMPONENT_NAME = 'App';

  // 注册组件
  registerComponent({
    componentName: COMPONENT_NAME,
    component: {
      state: {
        text: '欢迎使用Admin管理平台',
        username: '',
        password: '',
        loginError: ''
      },
      methods: {
        onLogin() {
          const { username, password } = this.state;
          if (!username || !password) {
            this.setState({ loginError: '用户名和密码不能为空' });
            return;
          }
          
          // 这里应该调用实际的登录接口
          console.log('登录请求:', { username, password });
          this.setState({ loginError: '演示环境，无需真实登录' });
        },
        onUsernameChange(value) {
          this.setState({ username: value });
        },
        onPasswordChange(value) {
          this.setState({ password: value });
        }
      },
      render() {
        return {
          componentName: 'Page',
          id: 'page1',
          props: {},
          children: [
            {
              componentName: 'Div',
              id: 'div1',
              props: {
                style: {
                  display: 'flex',
                  justifyContent: 'center',
                  alignItems: 'center',
                  height: '100vh'
                }
              },
              children: [
                {
                  componentName: 'Div',
                  id: 'loginForm',
                  props: {
                    style: {
                      width: 300,
                      padding: 20,
                      border: '1px solid #ddd',
                      borderRadius: 4
                    }
                  },
                  children: [
                    {
                      componentName: 'H2',
                      id: 'title',
                      props: {
                        style: {
                          textAlign: 'center'
                        }
                      },
                      children: [this.state.text]
                    },
                    {
                      componentName: 'Form',
                      id: 'form1',
                      props: {
                        fullWidth: true
                      },
                      children: [
                        {
                          componentName: 'Form.Item',
                          id: 'usernameItem',
                          props: {
                            label: '用户名',
                            required: true
                          },
                          children: [
                            {
                              componentName: 'Input',
                              id: 'username',
                              props: {
                                value: this.state.username,
                                onChange: { type: 'JSExpression', value: 'this.onUsernameChange.bind(this)' }
                              }
                            }
                          ]
                        },
                        {
                          componentName: 'Form.Item',
                          id: 'passwordItem',
                          props: {
                            label: '密码',
                            required: true
                          },
                          children: [
                            {
                              componentName: 'Input',
                              id: 'password',
                              props: {
                                value: this.state.password,
                                onChange: { type: 'JSExpression', value: 'this.onPasswordChange.bind(this)' },
                                htmlType: 'password'
                              }
                            }
                          ]
                        },
                        {
                          componentName: 'Form.Item',
                          id: 'buttonItem',
                          props: {},
                          children: [
                            {
                              componentName: 'Button',
                              id: 'loginBtn',
                              props: {
                                type: 'primary',
                                onClick: { type: 'JSExpression', value: 'this.onLogin.bind(this)' }
                              },
                              children: ['登录']
                            }
                          ]
                        },
                        this.state.loginError ? {
                          componentName: 'Div',
                          id: 'errorDiv',
                          props: {
                            style: {
                              color: 'red',
                              marginTop: 10
                            }
                          },
                          children: [this.state.loginError]
                        } : null
                      ].filter(Boolean)
                    }
                  ]
                }
              ]
            }
          ]
        };
      }
    }
  });

  // 初始化应用
  const initialState = getInitState();
  const { mount, unmount } = getInitAppLifeCycles(COMPONENT_NAME, initialState);

  // 导出生命周期方法
  win.appMount = mount;
  win.appUnmount = unmount;
})(window);